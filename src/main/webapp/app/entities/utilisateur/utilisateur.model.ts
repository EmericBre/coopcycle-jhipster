import { ICommerce } from 'app/entities/commerce/commerce.model';
import { ICommande } from 'app/entities/commande/commande.model';

export interface IUtilisateur {
  id?: string;
  firstname?: string;
  lastname?: string;
  mail?: string | null;
  phone?: string | null;
  address?: string;
  commerce?: ICommerce[] | null;
  commandes?: ICommande[] | null;
}

export class Utilisateur implements IUtilisateur {
  constructor(
    public id?: string,
    public firstname?: string,
    public lastname?: string,
    public mail?: string | null,
    public phone?: string | null,
    public address?: string,
    public commerce?: ICommerce[] | null,
    public commandes?: ICommande[] | null
  ) {}
}

export function getUtilisateurIdentifier(utilisateur: IUtilisateur): string | undefined {
  return utilisateur.id;
}
