import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'utilisateur',
        data: { pageTitle: 'coopcyclejhipsterApp.utilisateur.home.title' },
        loadChildren: () => import('./utilisateur/utilisateur.module').then(m => m.UtilisateurModule),
      },
      {
        path: 'produit',
        data: { pageTitle: 'coopcyclejhipsterApp.produit.home.title' },
        loadChildren: () => import('./produit/produit.module').then(m => m.ProduitModule),
      },
      {
        path: 'paiement',
        data: { pageTitle: 'coopcyclejhipsterApp.paiement.home.title' },
        loadChildren: () => import('./paiement/paiement.module').then(m => m.PaiementModule),
      },
      {
        path: 'commande',
        data: { pageTitle: 'coopcyclejhipsterApp.commande.home.title' },
        loadChildren: () => import('./commande/commande.module').then(m => m.CommandeModule),
      },
      {
        path: 'livreur',
        data: { pageTitle: 'coopcyclejhipsterApp.livreur.home.title' },
        loadChildren: () => import('./livreur/livreur.module').then(m => m.LivreurModule),
      },
      {
        path: 'cooperative',
        data: { pageTitle: 'coopcyclejhipsterApp.cooperative.home.title' },
        loadChildren: () => import('./cooperative/cooperative.module').then(m => m.CooperativeModule),
      },
      {
        path: 'commerce',
        data: { pageTitle: 'coopcyclejhipsterApp.commerce.home.title' },
        loadChildren: () => import('./commerce/commerce.module').then(m => m.CommerceModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
