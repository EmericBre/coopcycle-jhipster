import { ICommande } from 'app/entities/commande/commande.model';
import { IPaiement } from 'app/entities/paiement/paiement.model';
import { ICommerce } from 'app/entities/commerce/commerce.model';

export interface IProduit {
  id?: string;
  price?: number;
  type?: string;
  description?: string | null;
  commandes?: ICommande[] | null;
  paiement?: IPaiement | null;
  commerce?: ICommerce | null;
}

export class Produit implements IProduit {
  constructor(
    public id?: string,
    public price?: number,
    public type?: string,
    public description?: string | null,
    public commandes?: ICommande[] | null,
    public paiement?: IPaiement | null,
    public commerce?: ICommerce | null
  ) {}
}

export function getProduitIdentifier(produit: IProduit): string | undefined {
  return produit.id;
}
