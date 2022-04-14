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

describe('Commerce e2e test', () => {
  const commercePageUrl = '/commerce';
  const commercePageUrlPattern = new RegExp('/commerce(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const commerceSample = { id: '319cd5a0-347d-4909-9ce6-121ed7446afb', name: 'firewall Fresh interactive', adress: 'Georgia' };

  let commerce: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/commerce+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/commerce').as('postEntityRequest');
    cy.intercept('DELETE', '/api/commerce/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (commerce) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/commerce/${commerce.id}`,
      }).then(() => {
        commerce = undefined;
      });
    }
  });

  it('Commerce menu should load Commerce page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('commerce');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Commerce').should('exist');
    cy.url().should('match', commercePageUrlPattern);
  });

  describe('Commerce page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(commercePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Commerce page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/commerce/new$'));
        cy.getEntityCreateUpdateHeading('Commerce');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', commercePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/commerce',
          body: commerceSample,
        }).then(({ body }) => {
          commerce = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/commerce+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [commerce],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(commercePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Commerce page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('commerce');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', commercePageUrlPattern);
      });

      it('edit button click should load edit Commerce page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Commerce');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', commercePageUrlPattern);
      });

      it('last delete button click should delete instance of Commerce', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('commerce').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', commercePageUrlPattern);

        commerce = undefined;
      });
    });
  });

  describe('new Commerce page', () => {
    beforeEach(() => {
      cy.visit(`${commercePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Commerce');
    });

    it('should create an instance of Commerce', () => {
      cy.get(`[data-cy="id"]`).type('cc7f1050-7ca8-4db8-b2f3-f6071042470f').should('have.value', 'cc7f1050-7ca8-4db8-b2f3-f6071042470f');

      cy.get(`[data-cy="name"]`).type('Chicken').should('have.value', 'Chicken');

      cy.get(`[data-cy="adress"]`).type('Barbados Account synthesizing').should('have.value', 'Barbados Account synthesizing');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        commerce = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', commercePageUrlPattern);
    });
  });
});
