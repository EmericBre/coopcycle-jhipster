import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Livreur e2e test', () => {
  const livreurPageUrl = '/livreur';
  const livreurPageUrlPattern = new RegExp('/livreur(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const livreurSample = { id: '9c1db3c1-3a8e-4134-a5c2-4c1e8f6d82af', firstname: 'Jhg', lastname: 'Dy', phone: '(696) 953-4432 x947' };

  let livreur: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/livreurs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/livreurs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/livreurs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (livreur) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/livreurs/${livreur.id}`,
      }).then(() => {
        livreur = undefined;
      });
    }
  });

  it('Livreurs menu should load Livreurs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('livreur');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Livreur').should('exist');
    cy.url().should('match', livreurPageUrlPattern);
  });

  describe('Livreur page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(livreurPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Livreur page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/livreur/new$'));
        cy.getEntityCreateUpdateHeading('Livreur');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', livreurPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/livreurs',
          body: livreurSample,
        }).then(({ body }) => {
          livreur = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/livreurs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [livreur],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(livreurPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Livreur page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('livreur');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', livreurPageUrlPattern);
      });

      it('edit button click should load edit Livreur page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Livreur');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', livreurPageUrlPattern);
      });

      it('last delete button click should delete instance of Livreur', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('livreur').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', livreurPageUrlPattern);

        livreur = undefined;
      });
    });
  });

  describe('new Livreur page', () => {
    beforeEach(() => {
      cy.visit(`${livreurPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Livreur');
    });

    it('should create an instance of Livreur', () => {
      cy.get(`[data-cy="id"]`).type('8ea131e3-8cc3-4fbe-b065-ff02195eeee6').should('have.value', '8ea131e3-8cc3-4fbe-b065-ff02195eeee6');

      cy.get(`[data-cy="firstname"]`).type('Qu').should('have.value', 'Qu');

      cy.get(`[data-cy="lastname"]`).type('Ul').should('have.value', 'Ul');

      cy.get(`[data-cy="phone"]`).type('1-723-361-7935 x432').should('have.value', '1-723-361-7935 x432');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        livreur = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', livreurPageUrlPattern);
    });
  });
});
