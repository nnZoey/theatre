import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StageDetailComponent } from './stage-detail.component';

describe('Stage Management Detail Component', () => {
  let comp: StageDetailComponent;
  let fixture: ComponentFixture<StageDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StageDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ stage: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(StageDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(StageDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load stage on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.stage).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
