import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { ILivreur } from 'app/entities/livreur/livreur.model';
import { IProduit } from 'app/entities/produit/produit.model';

export interface ICommande {
  id?: string;
  utilisateur?: IUtilisateur | null;
  livreur?: ILivreur | null;
  produits?: IProduit[] | null;
}

export class Commande implements ICommande {
  constructor(
    public id?: string,
    public utilisateur?: IUtilisateur | null,
    public livreur?: ILivreur | null,
    public produits?: IProduit[] | null
  ) {}
}

export function getCommandeIdentifier(commande: ICommande): string | undefined {
  return commande.id;
}
