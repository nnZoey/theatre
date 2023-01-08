import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { StageFormService } from './stage-form.service';
import { StageService } from '../service/stage.service';
import { IStage } from '../stage.model';

import { StageUpdateComponent } from './stage-update.component';

describe('Stage Management Update Component', () => {
  let comp: StageUpdateComponent;
  let fixture: ComponentFixture<StageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let stageFormService: StageFormService;
  let stageService: StageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [StageUpdateComponent],
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
      .overrideTemplate(StageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    stageFormService = TestBed.inject(StageFormService);
    stageService = TestBed.inject(StageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const stage: IStage = { id: 456 };

      activatedRoute.data = of({ stage });
      comp.ngOnInit();

      expect(comp.stage).toEqual(stage);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStage>>();
      const stage = { id: 123 };
      jest.spyOn(stageFormService, 'getStage').mockReturnValue(stage);
      jest.spyOn(stageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stage }));
      saveSubject.complete();

      // THEN
      expect(stageFormService.getStage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(stageService.update).toHaveBeenCalledWith(expect.objectContaining(stage));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStage>>();
      const stage = { id: 123 };
      jest.spyOn(stageFormService, 'getStage').mockReturnValue({ id: null });
      jest.spyOn(stageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stage: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stage }));
      saveSubject.complete();

      // THEN
      expect(stageFormService.getStage).toHaveBeenCalled();
      expect(stageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStage>>();
      const stage = { id: 123 };
      jest.spyOn(stageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(stageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
