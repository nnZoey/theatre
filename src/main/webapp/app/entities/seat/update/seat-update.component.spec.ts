import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SeatFormService } from './seat-form.service';
import { SeatService } from '../service/seat.service';
import { ISeat } from '../seat.model';
import { IStage } from 'app/entities/stage/stage.model';
import { StageService } from 'app/entities/stage/service/stage.service';

import { SeatUpdateComponent } from './seat-update.component';

describe('Seat Management Update Component', () => {
  let comp: SeatUpdateComponent;
  let fixture: ComponentFixture<SeatUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let seatFormService: SeatFormService;
  let seatService: SeatService;
  let stageService: StageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SeatUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SeatUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SeatUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    seatFormService = TestBed.inject(SeatFormService);
    seatService = TestBed.inject(SeatService);
    stageService = TestBed.inject(StageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Stage query and add missing value', () => {
      const seat: ISeat = { id: 456 };
      const stage: IStage = { id: 1737 };
      seat.stage = stage;

      const stageCollection: IStage[] = [{ id: 40832 }];
      jest.spyOn(stageService, 'query').mockReturnValue(of(new HttpResponse({ body: stageCollection })));
      const additionalStages = [stage];
      const expectedCollection: IStage[] = [...additionalStages, ...stageCollection];
      jest.spyOn(stageService, 'addStageToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ seat });
      comp.ngOnInit();

      expect(stageService.query).toHaveBeenCalled();
      expect(stageService.addStageToCollectionIfMissing).toHaveBeenCalledWith(
        stageCollection,
        ...additionalStages.map(expect.objectContaining)
      );
      expect(comp.stagesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const seat: ISeat = { id: 456 };
      const stage: IStage = { id: 63428 };
      seat.stage = stage;

      activatedRoute.data = of({ seat });
      comp.ngOnInit();

      expect(comp.stagesSharedCollection).toContain(stage);
      expect(comp.seat).toEqual(seat);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISeat>>();
      const seat = { id: 123 };
      jest.spyOn(seatFormService, 'getSeat').mockReturnValue(seat);
      jest.spyOn(seatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ seat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: seat }));
      saveSubject.complete();

      // THEN
      expect(seatFormService.getSeat).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(seatService.update).toHaveBeenCalledWith(expect.objectContaining(seat));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISeat>>();
      const seat = { id: 123 };
      jest.spyOn(seatFormService, 'getSeat').mockReturnValue({ id: null });
      jest.spyOn(seatService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ seat: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: seat }));
      saveSubject.complete();

      // THEN
      expect(seatFormService.getSeat).toHaveBeenCalled();
      expect(seatService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISeat>>();
      const seat = { id: 123 };
      jest.spyOn(seatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ seat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(seatService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareStage', () => {
      it('Should forward to stageService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(stageService, 'compareStage');
        comp.compareStage(entity, entity2);
        expect(stageService.compareStage).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
