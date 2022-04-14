import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICommande, Commande } from '../commande.model';

import { CommandeService } from './commande.service';

describe('Commande Service', () => {
  let service: CommandeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICommande;
  let expectedResult: ICommande | ICommande[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CommandeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
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

    it('should create a Commande', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Commande()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Commande', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Commande', () => {
      const patchObject = Object.assign({}, new Commande());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Commande', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
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

    it('should delete a Commande', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCommandeToCollectionIfMissing', () => {
      it('should add a Commande to an empty array', () => {
        const commande: ICommande = { id: 'ABC' };
        expectedResult = service.addCommandeToCollectionIfMissing([], commande);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commande);
      });

      it('should not add a Commande to an array that contains it', () => {
        const commande: ICommande = { id: 'ABC' };
        const commandeCollection: ICommande[] = [
          {
            ...commande,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addCommandeToCollectionIfMissing(commandeCollection, commande);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Commande to an array that doesn't contain it", () => {
        const commande: ICommande = { id: 'ABC' };
        const commandeCollection: ICommande[] = [{ id: 'CBA' }];
        expectedResult = service.addCommandeToCollectionIfMissing(commandeCollection, commande);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commande);
      });

      it('should add only unique Commande to an array', () => {
        const commandeArray: ICommande[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'cc66c3d6-9f82-483b-b4ee-9a5ff5f1d277' }];
        const commandeCollection: ICommande[] = [{ id: 'ABC' }];
        expectedResult = service.addCommandeToCollectionIfMissing(commandeCollection, ...commandeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const commande: ICommande = { id: 'ABC' };
        const commande2: ICommande = { id: 'CBA' };
        expectedResult = service.addCommandeToCollectionIfMissing([], commande, commande2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commande);
        expect(expectedResult).toContain(commande2);
      });

      it('should accept null and undefined values', () => {
        const commande: ICommande = { id: 'ABC' };
        expectedResult = service.addCommandeToCollectionIfMissing([], null, commande, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commande);
      });

      it('should return initial array if no Commande is added', () => {
        const commandeCollection: ICommande[] = [{ id: 'ABC' }];
        expectedResult = service.addCommandeToCollectionIfMissing(commandeCollection, undefined, null);
        expect(expectedResult).toEqual(commandeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
