import { ICommerce } from 'app/entities/commerce/commerce.model';

export interface ICooperative {
  id?: string;
  name?: string;
  adress?: string | null;
  commerce?: ICommerce[] | null;
}

export class Cooperative implements ICooperative {
  constructor(public id?: string, public name?: string, public adress?: string | null, public commerce?: ICommerce[] | null) {}
}

export function getCooperativeIdentifier(cooperative: ICooperative): string | undefined {
  return cooperative.id;
}
