import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EventTypeFormService } from './event-type-form.service';
import { EventTypeService } from '../service/event-type.service';
import { IEventType } from '../event-type.model';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';

import { EventTypeUpdateComponent } from './event-type-update.component';

describe('EventType Management Update Component', () => {
  let comp: EventTypeUpdateComponent;
  let fixture: ComponentFixture<EventTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let eventTypeFormService: EventTypeFormService;
  let eventTypeService: EventTypeService;
  let eventService: EventService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EventTypeUpdateComponent],
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
      .overrideTemplate(EventTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EventTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    eventTypeFormService = TestBed.inject(EventTypeFormService);
    eventTypeService = TestBed.inject(EventTypeService);
    eventService = TestBed.inject(EventService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Event query and add missing value', () => {
      const eventType: IEventType = { id: 456 };
      const event: IEvent = { id: 24481 };
      eventType.event = event;

      const eventCollection: IEvent[] = [{ id: 70440 }];
      jest.spyOn(eventService, 'query').mockReturnValue(of(new HttpResponse({ body: eventCollection })));
      const additionalEvents = [event];
      const expectedCollection: IEvent[] = [...additionalEvents, ...eventCollection];
      jest.spyOn(eventService, 'addEventToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eventType });
      comp.ngOnInit();

      expect(eventService.query).toHaveBeenCalled();
      expect(eventService.addEventToCollectionIfMissing).toHaveBeenCalledWith(
        eventCollection,
        ...additionalEvents.map(expect.objectContaining)
      );
      expect(comp.eventsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const eventType: IEventType = { id: 456 };
      const event: IEvent = { id: 583 };
      eventType.event = event;

      activatedRoute.data = of({ eventType });
      comp.ngOnInit();

      expect(comp.eventsSharedCollection).toContain(event);
      expect(comp.eventType).toEqual(eventType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEventType>>();
      const eventType = { id: 123 };
      jest.spyOn(eventTypeFormService, 'getEventType').mockReturnValue(eventType);
      jest.spyOn(eventTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eventType }));
      saveSubject.complete();

      // THEN
      expect(eventTypeFormService.getEventType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(eventTypeService.update).toHaveBeenCalledWith(expect.objectContaining(eventType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEventType>>();
      const eventType = { id: 123 };
      jest.spyOn(eventTypeFormService, 'getEventType').mockReturnValue({ id: null });
      jest.spyOn(eventTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eventType }));
      saveSubject.complete();

      // THEN
      expect(eventTypeFormService.getEventType).toHaveBeenCalled();
      expect(eventTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEventType>>();
      const eventType = { id: 123 };
      jest.spyOn(eventTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(eventTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEvent', () => {
      it('Should forward to eventService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(eventService, 'compareEvent');
        comp.compareEvent(entity, entity2);
        expect(eventService.compareEvent).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
