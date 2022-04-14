import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICommerce, getCommerceIdentifier } from '../commerce.model';

export type EntityResponseType = HttpResponse<ICommerce>;
export type EntityArrayResponseType = HttpResponse<ICommerce[]>;

@Injectable({ providedIn: 'root' })
export class CommerceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/commerce');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(commerce: ICommerce): Observable<EntityResponseType> {
    return this.http.post<ICommerce>(this.resourceUrl, commerce, { observe: 'response' });
  }

  update(commerce: ICommerce): Observable<EntityResponseType> {
    return this.http.put<ICommerce>(`${this.resourceUrl}/${getCommerceIdentifier(commerce) as string}`, commerce, { observe: 'response' });
  }

  partialUpdate(commerce: ICommerce): Observable<EntityResponseType> {
    return this.http.patch<ICommerce>(`${this.resourceUrl}/${getCommerceIdentifier(commerce) as string}`, commerce, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ICommerce>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICommerce[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCommerceToCollectionIfMissing(commerceCollection: ICommerce[], ...commerceToCheck: (ICommerce | null | undefined)[]): ICommerce[] {
    const commerce: ICommerce[] = commerceToCheck.filter(isPresent);
    if (commerce.length > 0) {
      const commerceCollectionIdentifiers = commerceCollection.map(commerceItem => getCommerceIdentifier(commerceItem)!);
      const commerceToAdd = commerce.filter(commerceItem => {
        const commerceIdentifier = getCommerceIdentifier(commerceItem);
        if (commerceIdentifier == null || commerceCollectionIdentifiers.includes(commerceIdentifier)) {
          return false;
        }
        commerceCollectionIdentifiers.push(commerceIdentifier);
        return true;
      });
      return [...commerceToAdd, ...commerceCollection];
    }
    return commerceCollection;
  }
}
