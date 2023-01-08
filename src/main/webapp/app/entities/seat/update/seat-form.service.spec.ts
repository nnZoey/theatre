import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../seat.test-samples';

import { SeatFormService } from './seat-form.service';

describe('Seat Form Service', () => {
  let service: SeatFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SeatFormService);
  });

  describe('Service methods', () => {
    describe('createSeatFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSeatFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            row: expect.any(Object),
            col: expect.any(Object),
            seatClass: expect.any(Object),
            stage: expect.any(Object),
          })
        );
      });

      it('passing ISeat should create a new form with FormGroup', () => {
        const formGroup = service.createSeatFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            row: expect.any(Object),
            col: expect.any(Object),
            seatClass: expect.any(Object),
            stage: expect.any(Object),
          })
        );
      });
    });

    describe('getSeat', () => {
      it('should return NewSeat for default Seat initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSeatFormGroup(sampleWithNewData);

        const seat = service.getSeat(formGroup) as any;

        expect(seat).toMatchObject(sampleWithNewData);
      });

      it('should return NewSeat for empty Seat initial value', () => {
        const formGroup = service.createSeatFormGroup();

        const seat = service.getSeat(formGroup) as any;

        expect(seat).toMatchObject({});
      });

      it('should return ISeat', () => {
        const formGroup = service.createSeatFormGroup(sampleWithRequiredData);

        const seat = service.getSeat(formGroup) as any;

        expect(seat).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISeat should not enable id FormControl', () => {
        const formGroup = service.createSeatFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSeat should disable id FormControl', () => {
        const formGroup = service.createSeatFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
