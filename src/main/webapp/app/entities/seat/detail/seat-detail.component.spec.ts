import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SeatDetailComponent } from './seat-detail.component';

describe('Seat Management Detail Component', () => {
  let comp: SeatDetailComponent;
  let fixture: ComponentFixture<SeatDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SeatDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ seat: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SeatDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SeatDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load seat on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.seat).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
