import { ICommande } from 'app/entities/commande/commande.model';

export interface ILivreur {
  id?: string;
  firstname?: string;
  lastname?: string;
  phone?: string;
  commandes?: ICommande[] | null;
}

export class Livreur implements ILivreur {
  constructor(
    public id?: string,
    public firstname?: string,
    public lastname?: string,
    public phone?: string,
    public commandes?: ICommande[] | null
  ) {}
}

export function getLivreurIdentifier(livreur: ILivreur): string | undefined {
  return livreur.id;
}
