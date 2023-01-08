import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IStage, NewStage } from '../stage.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStage for edit and NewStageFormGroupInput for create.
 */
type StageFormGroupInput = IStage | PartialWithRequiredKeyOf<NewStage>;

type StageFormDefaults = Pick<NewStage, 'id'>;

type StageFormGroupContent = {
  id: FormControl<IStage['id'] | NewStage['id']>;
  name: FormControl<IStage['name']>;
  location: FormControl<IStage['location']>;
};

export type StageFormGroup = FormGroup<StageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StageFormService {
  createStageFormGroup(stage: StageFormGroupInput = { id: null }): StageFormGroup {
    const stageRawValue = {
      ...this.getFormDefaults(),
      ...stage,
    };
    return new FormGroup<StageFormGroupContent>({
      id: new FormControl(
        { value: stageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(stageRawValue.name),
      location: new FormControl(stageRawValue.location),
    });
  }

  getStage(form: StageFormGroup): IStage | NewStage {
    return form.getRawValue() as IStage | NewStage;
  }

  resetForm(form: StageFormGroup, stage: StageFormGroupInput): void {
    const stageRawValue = { ...this.getFormDefaults(), ...stage };
    form.reset(
      {
        ...stageRawValue,
        id: { value: stageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): StageFormDefaults {
    return {
      id: null,
    };
  }
}
