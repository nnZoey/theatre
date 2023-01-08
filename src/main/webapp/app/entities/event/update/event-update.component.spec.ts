import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EventFormService } from './event-form.service';
import { EventService } from '../service/event.service';
import { IEvent } from '../event.model';
import { IEventType } from 'app/entities/event-type/event-type.model';
import { EventTypeService } from 'app/entities/event-type/service/event-type.service';
import { IStage } from 'app/entities/stage/stage.model';
import { StageService } from 'app/entities/stage/service/stage.service';

import { EventUpdateComponent } from './event-update.component';

describe('Event Management Update Component', () => {
  let comp: EventUpdateComponent;
  let fixture: ComponentFixture<EventUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let eventFormService: EventFormService;
  let eventService: EventService;
  let eventTypeService: EventTypeService;
  let stageService: StageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EventUpdateComponent],
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
      .overrideTemplate(EventUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EventUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    eventFormService = TestBed.inject(EventFormService);
    eventService = TestBed.inject(EventService);
    eventTypeService = TestBed.inject(EventTypeService);
    stageService = TestBed.inject(StageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call EventType query and add missing value', () => {
      const event: IEvent = { id: 456 };
      const eventType: IEventType = { id: 15975 };
      event.eventType = eventType;

      const eventTypeCollection: IEventType[] = [{ id: 37790 }];
      jest.spyOn(eventTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: eventTypeCollection })));
      const additionalEventTypes = [eventType];
      const expectedCollection: IEventType[] = [...additionalEventTypes, ...eventTypeCollection];
      jest.spyOn(eventTypeService, 'addEventTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ event });
      comp.ngOnInit();

      expect(eventTypeService.query).toHaveBeenCalled();
      expect(eventTypeService.addEventTypeToCollectionIfMissing).toHaveBeenCalledWith(
        eventTypeCollection,
        ...additionalEventTypes.map(expect.objectContaining)
      );
      expect(comp.eventTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Stage query and add missing value', () => {
      const event: IEvent = { id: 456 };
      const stage: IStage = { id: 34225 };
      event.stage = stage;

      const stageCollection: IStage[] = [{ id: 54853 }];
      jest.spyOn(stageService, 'query').mockReturnValue(of(new HttpResponse({ body: stageCollection })));
      const additionalStages = [stage];
      const expectedCollection: IStage[] = [...additionalStages, ...stageCollection];
      jest.spyOn(stageService, 'addStageToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ event });
      comp.ngOnInit();

      expect(stageService.query).toHaveBeenCalled();
      expect(stageService.addStageToCollectionIfMissing).toHaveBeenCalledWith(
        stageCollection,
        ...additionalStages.map(expect.objectContaining)
      );
      expect(comp.stagesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const event: IEvent = { id: 456 };
      const eventType: IEventType = { id: 82567 };
      event.eventType = eventType;
      const stage: IStage = { id: 63344 };
      event.stage = stage;

      activatedRoute.data = of({ event });
      comp.ngOnInit();

      expect(comp.eventTypesSharedCollection).toContain(eventType);
      expect(comp.stagesSharedCollection).toContain(stage);
      expect(comp.event).toEqual(event);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvent>>();
      const event = { id: 123 };
      jest.spyOn(eventFormService, 'getEvent').mockReturnValue(event);
      jest.spyOn(eventService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ event });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: event }));
      saveSubject.complete();

      // THEN
      expect(eventFormService.getEvent).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(eventService.update).toHaveBeenCalledWith(expect.objectContaining(event));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvent>>();
      const event = { id: 123 };
      jest.spyOn(eventFormService, 'getEvent').mockReturnValue({ id: null });
      jest.spyOn(eventService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ event: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: event }));
      saveSubject.complete();

      // THEN
      expect(eventFormService.getEvent).toHaveBeenCalled();
      expect(eventService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvent>>();
      const event = { id: 123 };
      jest.spyOn(eventService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ event });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(eventService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEventType', () => {
      it('Should forward to eventTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(eventTypeService, 'compareEventType');
        comp.compareEventType(entity, entity2);
        expect(eventTypeService.compareEventType).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
