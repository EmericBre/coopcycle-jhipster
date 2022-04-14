import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILivreur, getLivreurIdentifier } from '../livreur.model';

export type EntityResponseType = HttpResponse<ILivreur>;
export type EntityArrayResponseType = HttpResponse<ILivreur[]>;

@Injectable({ providedIn: 'root' })
export class LivreurService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/livreurs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(livreur: ILivreur): Observable<EntityResponseType> {
    return this.http.post<ILivreur>(this.resourceUrl, livreur, { observe: 'response' });
  }

  update(livreur: ILivreur): Observable<EntityResponseType> {
    return this.http.put<ILivreur>(`${this.resourceUrl}/${getLivreurIdentifier(livreur) as string}`, livreur, { observe: 'response' });
  }

  partialUpdate(livreur: ILivreur): Observable<EntityResponseType> {
    return this.http.patch<ILivreur>(`${this.resourceUrl}/${getLivreurIdentifier(livreur) as string}`, livreur, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ILivreur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILivreur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLivreurToCollectionIfMissing(livreurCollection: ILivreur[], ...livreursToCheck: (ILivreur | null | undefined)[]): ILivreur[] {
    const livreurs: ILivreur[] = livreursToCheck.filter(isPresent);
    if (livreurs.length > 0) {
      const livreurCollectionIdentifiers = livreurCollection.map(livreurItem => getLivreurIdentifier(livreurItem)!);
      const livreursToAdd = livreurs.filter(livreurItem => {
        const livreurIdentifier = getLivreurIdentifier(livreurItem);
        if (livreurIdentifier == null || livreurCollectionIdentifiers.includes(livreurIdentifier)) {
          return false;
        }
        livreurCollectionIdentifiers.push(livreurIdentifier);
        return true;
      });
      return [...livreursToAdd, ...livreurCollection];
    }
    return livreurCollection;
  }
}
