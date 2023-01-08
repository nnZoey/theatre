import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../event.test-samples';

import { EventFormService } from './event-form.service';

describe('Event Form Service', () => {
  let service: EventFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EventFormService);
  });

  describe('Service methods', () => {
    describe('createEventFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEventFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            ageRestriction: expect.any(Object),
            status: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            dateBefore: expect.any(Object),
            image: expect.any(Object),
            eventType: expect.any(Object),
            stage: expect.any(Object),
            createdBy: expect.any(Object),
          })
        );
      });

      it('passing IEvent should create a new form with FormGroup', () => {
        const formGroup = service.createEventFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            ageRestriction: expect.any(Object),
            status: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            dateBefore: expect.any(Object),
            image: expect.any(Object),
            eventType: expect.any(Object),
            stage: expect.any(Object),
            createdBy: expect.any(Object),
          })
        );
      });
    });

    describe('getEvent', () => {
      it('should return NewEvent for default Event initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEventFormGroup(sampleWithNewData);

        const event = service.getEvent(formGroup) as any;

        expect(event).toMatchObject(sampleWithNewData);
      });

      it('should return NewEvent for empty Event initial value', () => {
        const formGroup = service.createEventFormGroup();

        const event = service.getEvent(formGroup) as any;

        expect(event).toMatchObject({});
      });

      it('should return IEvent', () => {
        const formGroup = service.createEventFormGroup(sampleWithRequiredData);

        const event = service.getEvent(formGroup) as any;

        expect(event).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEvent should not enable id FormControl', () => {
        const formGroup = service.createEventFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEvent should disable id FormControl', () => {
        const formGroup = service.createEventFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
