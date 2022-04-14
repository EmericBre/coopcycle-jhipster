import { IProduit } from 'app/entities/produit/produit.model';
import { ICooperative } from 'app/entities/cooperative/cooperative.model';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';

export interface ICommerce {
  id?: string;
  name?: string;
  adress?: string;
  produits?: IProduit[] | null;
  cooperatives?: ICooperative[] | null;
  utilisateur?: IUtilisateur | null;
}

export class Commerce implements ICommerce {
  constructor(
    public id?: string,
    public name?: string,
    public adress?: string,
    public produits?: IProduit[] | null,
    public cooperatives?: ICooperative[] | null,
    public utilisateur?: IUtilisateur | null
  ) {}
}

export function getCommerceIdentifier(commerce: ICommerce): string | undefined {
  return commerce.id;
}
