import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISeat } from '../seat.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../seat.test-samples';

import { SeatService } from './seat.service';

const requireRestSample: ISeat = {
  ...sampleWithRequiredData,
};

describe('Seat Service', () => {
  let service: SeatService;
  let httpMock: HttpTestingController;
  let expectedResult: ISeat | ISeat[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SeatService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Seat', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const seat = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(seat).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Seat', () => {
      const seat = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(seat).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Seat', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Seat', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Seat', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSeatToCollectionIfMissing', () => {
      it('should add a Seat to an empty array', () => {
        const seat: ISeat = sampleWithRequiredData;
        expectedResult = service.addSeatToCollectionIfMissing([], seat);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(seat);
      });

      it('should not add a Seat to an array that contains it', () => {
        const seat: ISeat = sampleWithRequiredData;
        const seatCollection: ISeat[] = [
          {
            ...seat,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSeatToCollectionIfMissing(seatCollection, seat);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Seat to an array that doesn't contain it", () => {
        const seat: ISeat = sampleWithRequiredData;
        const seatCollection: ISeat[] = [sampleWithPartialData];
        expectedResult = service.addSeatToCollectionIfMissing(seatCollection, seat);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(seat);
      });

      it('should add only unique Seat to an array', () => {
        const seatArray: ISeat[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const seatCollection: ISeat[] = [sampleWithRequiredData];
        expectedResult = service.addSeatToCollectionIfMissing(seatCollection, ...seatArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const seat: ISeat = sampleWithRequiredData;
        const seat2: ISeat = sampleWithPartialData;
        expectedResult = service.addSeatToCollectionIfMissing([], seat, seat2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(seat);
        expect(expectedResult).toContain(seat2);
      });

      it('should accept null and undefined values', () => {
        const seat: ISeat = sampleWithRequiredData;
        expectedResult = service.addSeatToCollectionIfMissing([], null, seat, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(seat);
      });

      it('should return initial array if no Seat is added', () => {
        const seatCollection: ISeat[] = [sampleWithRequiredData];
        expectedResult = service.addSeatToCollectionIfMissing(seatCollection, undefined, null);
        expect(expectedResult).toEqual(seatCollection);
      });
    });

    describe('compareSeat', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSeat(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSeat(entity1, entity2);
        const compareResult2 = service.compareSeat(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSeat(entity1, entity2);
        const compareResult2 = service.compareSeat(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSeat(entity1, entity2);
        const compareResult2 = service.compareSeat(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
