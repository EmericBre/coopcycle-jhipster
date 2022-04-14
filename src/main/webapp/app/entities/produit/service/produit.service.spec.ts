import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProduit, Produit } from '../produit.model';

import { ProduitService } from './produit.service';

describe('Produit Service', () => {
  let service: ProduitService;
  let httpMock: HttpTestingController;
  let elemDefault: IProduit;
  let expectedResult: IProduit | IProduit[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProduitService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
      price: 0,
      type: 'AAAAAAA',
      description: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Produit', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Produit()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Produit', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          price: 1,
          type: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Produit', () => {
      const patchObject = Object.assign(
        {
          price: 1,
          type: 'BBBBBB',
        },
        new Produit()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Produit', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          price: 1,
          type: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Produit', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addProduitToCollectionIfMissing', () => {
      it('should add a Produit to an empty array', () => {
        const produit: IProduit = { id: 'ABC' };
        expectedResult = service.addProduitToCollectionIfMissing([], produit);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(produit);
      });

      it('should not add a Produit to an array that contains it', () => {
        const produit: IProduit = { id: 'ABC' };
        const produitCollection: IProduit[] = [
          {
            ...produit,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addProduitToCollectionIfMissing(produitCollection, produit);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Produit to an array that doesn't contain it", () => {
        const produit: IProduit = { id: 'ABC' };
        const produitCollection: IProduit[] = [{ id: 'CBA' }];
        expectedResult = service.addProduitToCollectionIfMissing(produitCollection, produit);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(produit);
      });

      it('should add only unique Produit to an array', () => {
        const produitArray: IProduit[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '58bd0432-18e0-4cdc-9264-7bb16bead479' }];
        const produitCollection: IProduit[] = [{ id: 'ABC' }];
        expectedResult = service.addProduitToCollectionIfMissing(produitCollection, ...produitArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const produit: IProduit = { id: 'ABC' };
        const produit2: IProduit = { id: 'CBA' };
        expectedResult = service.addProduitToCollectionIfMissing([], produit, produit2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(produit);
        expect(expectedResult).toContain(produit2);
      });

      it('should accept null and undefined values', () => {
        const produit: IProduit = { id: 'ABC' };
        expectedResult = service.addProduitToCollectionIfMissing([], null, produit, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(produit);
      });

      it('should return initial array if no Produit is added', () => {
        const produitCollection: IProduit[] = [{ id: 'ABC' }];
        expectedResult = service.addProduitToCollectionIfMissing(produitCollection, undefined, null);
        expect(expectedResult).toEqual(produitCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
