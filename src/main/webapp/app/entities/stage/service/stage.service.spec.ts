import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IStage } from '../stage.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../stage.test-samples';

import { StageService } from './stage.service';

const requireRestSample: IStage = {
  ...sampleWithRequiredData,
};

describe('Stage Service', () => {
  let service: StageService;
  let httpMock: HttpTestingController;
  let expectedResult: IStage | IStage[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StageService);
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

    it('should create a Stage', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const stage = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(stage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Stage', () => {
      const stage = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(stage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Stage', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Stage', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Stage', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addStageToCollectionIfMissing', () => {
      it('should add a Stage to an empty array', () => {
        const stage: IStage = sampleWithRequiredData;
        expectedResult = service.addStageToCollectionIfMissing([], stage);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stage);
      });

      it('should not add a Stage to an array that contains it', () => {
        const stage: IStage = sampleWithRequiredData;
        const stageCollection: IStage[] = [
          {
            ...stage,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStageToCollectionIfMissing(stageCollection, stage);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Stage to an array that doesn't contain it", () => {
        const stage: IStage = sampleWithRequiredData;
        const stageCollection: IStage[] = [sampleWithPartialData];
        expectedResult = service.addStageToCollectionIfMissing(stageCollection, stage);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stage);
      });

      it('should add only unique Stage to an array', () => {
        const stageArray: IStage[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const stageCollection: IStage[] = [sampleWithRequiredData];
        expectedResult = service.addStageToCollectionIfMissing(stageCollection, ...stageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const stage: IStage = sampleWithRequiredData;
        const stage2: IStage = sampleWithPartialData;
        expectedResult = service.addStageToCollectionIfMissing([], stage, stage2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stage);
        expect(expectedResult).toContain(stage2);
      });

      it('should accept null and undefined values', () => {
        const stage: IStage = sampleWithRequiredData;
        expectedResult = service.addStageToCollectionIfMissing([], null, stage, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stage);
      });

      it('should return initial array if no Stage is added', () => {
        const stageCollection: IStage[] = [sampleWithRequiredData];
        expectedResult = service.addStageToCollectionIfMissing(stageCollection, undefined, null);
        expect(expectedResult).toEqual(stageCollection);
      });
    });

    describe('compareStage', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStage(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareStage(entity1, entity2);
        const compareResult2 = service.compareStage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareStage(entity1, entity2);
        const compareResult2 = service.compareStage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareStage(entity1, entity2);
        const compareResult2 = service.compareStage(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
