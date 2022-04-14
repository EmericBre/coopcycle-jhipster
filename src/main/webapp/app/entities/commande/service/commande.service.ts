import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICommande, getCommandeIdentifier } from '../commande.model';

export type EntityResponseType = HttpResponse<ICommande>;
export type EntityArrayResponseType = HttpResponse<ICommande[]>;

@Injectable({ providedIn: 'root' })
export class CommandeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/commandes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(commande: ICommande): Observable<EntityResponseType> {
    return this.http.post<ICommande>(this.resourceUrl, commande, { observe: 'response' });
  }

  update(commande: ICommande): Observable<EntityResponseType> {
    return this.http.put<ICommande>(`${this.resourceUrl}/${getCommandeIdentifier(commande) as string}`, commande, { observe: 'response' });
  }

  partialUpdate(commande: ICommande): Observable<EntityResponseType> {
    return this.http.patch<ICommande>(`${this.resourceUrl}/${getCommandeIdentifier(commande) as string}`, commande, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ICommande>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICommande[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCommandeToCollectionIfMissing(commandeCollection: ICommande[], ...commandesToCheck: (ICommande | null | undefined)[]): ICommande[] {
    const commandes: ICommande[] = commandesToCheck.filter(isPresent);
    if (commandes.length > 0) {
      const commandeCollectionIdentifiers = commandeCollection.map(commandeItem => getCommandeIdentifier(commandeItem)!);
      const commandesToAdd = commandes.filter(commandeItem => {
        const commandeIdentifier = getCommandeIdentifier(commandeItem);
        if (commandeIdentifier == null || commandeCollectionIdentifiers.includes(commandeIdentifier)) {
          return false;
        }
        commandeCollectionIdentifiers.push(commandeIdentifier);
        return true;
      });
      return [...commandesToAdd, ...commandeCollection];
    }
    return commandeCollection;
  }
}
