import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICommerce, Commerce } from '../commerce.model';

import { CommerceService } from './commerce.service';

describe('Commerce Service', () => {
  let service: CommerceService;
  let httpMock: HttpTestingController;
  let elemDefault: ICommerce;
  let expectedResult: ICommerce | ICommerce[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CommerceService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
      name: 'AAAAAAA',
      adress: 'AAAAAAA',
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

    it('should create a Commerce', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Commerce()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Commerce', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          name: 'BBBBBB',
          adress: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Commerce', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new Commerce()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Commerce', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          name: 'BBBBBB',
          adress: 'BBBBBB',
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

    it('should delete a Commerce', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCommerceToCollectionIfMissing', () => {
      it('should add a Commerce to an empty array', () => {
        const commerce: ICommerce = { id: 'ABC' };
        expectedResult = service.addCommerceToCollectionIfMissing([], commerce);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commerce);
      });

      it('should not add a Commerce to an array that contains it', () => {
        const commerce: ICommerce = { id: 'ABC' };
        const commerceCollection: ICommerce[] = [
          {
            ...commerce,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addCommerceToCollectionIfMissing(commerceCollection, commerce);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Commerce to an array that doesn't contain it", () => {
        const commerce: ICommerce = { id: 'ABC' };
        const commerceCollection: ICommerce[] = [{ id: 'CBA' }];
        expectedResult = service.addCommerceToCollectionIfMissing(commerceCollection, commerce);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commerce);
      });

      it('should add only unique Commerce to an array', () => {
        const commerceArray: ICommerce[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '6adde4c3-6efe-422d-8c29-9be0dba6e859' }];
        const commerceCollection: ICommerce[] = [{ id: 'ABC' }];
        expectedResult = service.addCommerceToCollectionIfMissing(commerceCollection, ...commerceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const commerce: ICommerce = { id: 'ABC' };
        const commerce2: ICommerce = { id: 'CBA' };
        expectedResult = service.addCommerceToCollectionIfMissing([], commerce, commerce2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commerce);
        expect(expectedResult).toContain(commerce2);
      });

      it('should accept null and undefined values', () => {
        const commerce: ICommerce = { id: 'ABC' };
        expectedResult = service.addCommerceToCollectionIfMissing([], null, commerce, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commerce);
      });

      it('should return initial array if no Commerce is added', () => {
        const commerceCollection: ICommerce[] = [{ id: 'ABC' }];
        expectedResult = service.addCommerceToCollectionIfMissing(commerceCollection, undefined, null);
        expect(expectedResult).toEqual(commerceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
