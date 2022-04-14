import { IProduit } from 'app/entities/produit/produit.model';

export interface IPaiement {
  id?: number;
  amount?: number;
  produit?: IProduit | null;
}

export class Paiement implements IPaiement {
  constructor(public id?: number, public amount?: number, public produit?: IProduit | null) {}
}

export function getPaiementIdentifier(paiement: IPaiement): number | undefined {
  return paiement.id;
}
