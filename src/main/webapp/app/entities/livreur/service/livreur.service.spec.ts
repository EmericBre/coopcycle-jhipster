import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILivreur, Livreur } from '../livreur.model';

import { LivreurService } from './livreur.service';

describe('Livreur Service', () => {
  let service: LivreurService;
  let httpMock: HttpTestingController;
  let elemDefault: ILivreur;
  let expectedResult: ILivreur | ILivreur[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LivreurService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
      firstname: 'AAAAAAA',
      lastname: 'AAAAAAA',
      phone: 'AAAAAAA',
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

    it('should create a Livreur', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Livreur()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Livreur', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          firstname: 'BBBBBB',
          lastname: 'BBBBBB',
          phone: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Livreur', () => {
      const patchObject = Object.assign(
        {
          firstname: 'BBBBBB',
          phone: 'BBBBBB',
        },
        new Livreur()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Livreur', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          firstname: 'BBBBBB',
          lastname: 'BBBBBB',
          phone: 'BBBBBB',
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

    it('should delete a Livreur', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLivreurToCollectionIfMissing', () => {
      it('should add a Livreur to an empty array', () => {
        const livreur: ILivreur = { id: 'ABC' };
        expectedResult = service.addLivreurToCollectionIfMissing([], livreur);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(livreur);
      });

      it('should not add a Livreur to an array that contains it', () => {
        const livreur: ILivreur = { id: 'ABC' };
        const livreurCollection: ILivreur[] = [
          {
            ...livreur,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addLivreurToCollectionIfMissing(livreurCollection, livreur);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Livreur to an array that doesn't contain it", () => {
        const livreur: ILivreur = { id: 'ABC' };
        const livreurCollection: ILivreur[] = [{ id: 'CBA' }];
        expectedResult = service.addLivreurToCollectionIfMissing(livreurCollection, livreur);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(livreur);
      });

      it('should add only unique Livreur to an array', () => {
        const livreurArray: ILivreur[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'e0d2698f-7908-4ac2-b2a3-029d8cddd36c' }];
        const livreurCollection: ILivreur[] = [{ id: 'ABC' }];
        expectedResult = service.addLivreurToCollectionIfMissing(livreurCollection, ...livreurArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const livreur: ILivreur = { id: 'ABC' };
        const livreur2: ILivreur = { id: 'CBA' };
        expectedResult = service.addLivreurToCollectionIfMissing([], livreur, livreur2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(livreur);
        expect(expectedResult).toContain(livreur2);
      });

      it('should accept null and undefined values', () => {
        const livreur: ILivreur = { id: 'ABC' };
        expectedResult = service.addLivreurToCollectionIfMissing([], null, livreur, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(livreur);
      });

      it('should return initial array if no Livreur is added', () => {
        const livreurCollection: ILivreur[] = [{ id: 'ABC' }];
        expectedResult = service.addLivreurToCollectionIfMissing(livreurCollection, undefined, null);
        expect(expectedResult).toEqual(livreurCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
