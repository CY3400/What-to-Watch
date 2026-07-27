import { Component, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { PublicLayout } from '../../shared/components/public-layout/public-layout';
import { Icon } from '../../shared/components/icon/icon';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { passwordMatchValidator, passwordStrengthValidator } from '../../shared/validators/password-validator';
import { RegisterRequest } from '../../models/auth.models';
import { Auth } from '../../services/auth';
import { finalize } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-register',
  imports: [RouterLink, PublicLayout, Icon, ReactiveFormsModule],
  templateUrl: './register.html'
})
export class Register {
  showPassword: boolean = false;
  showConfirmPassword: boolean = false;

  registerForm = new FormGroup (
    {
      email: new FormControl('', {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.email,
          Validators.maxLength(190)
        ]
      }),
      password: new FormControl('', {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.minLength(10),
          Validators.maxLength(64),
          passwordStrengthValidator()
        ]
      }),
      confirmPassword: new FormControl('', {
        nonNullable: true,
        validators: [
          Validators.required
        ]
      })
    },
    {
      validators: passwordMatchValidator()
    }
  );

  readonly isSubmitting = signal(false);
  readonly serverError = signal('');

  constructor(private auth: Auth, private router: Router) {}

  protected togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  protected toggleConfirmPassword(): void {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  private validateSubmit(): boolean {
    this.registerForm.markAllAsTouched();
    return this.registerForm.valid;
  }

  protected onSubmit(): void {
    if(this.isSubmitting()) {
      return;
    }

    this.serverError.set('');

    if(!this.validateSubmit()) {
      return;
    }

    this.isSubmitting.set(true);

    const formValue = this.registerForm.getRawValue();
    const user: RegisterRequest = {
      email: formValue.email.trim(),
      password: formValue.password
    };

    this.auth.register(user).pipe(finalize(() => {this.isSubmitting.set(false);})).subscribe({
      next: () => {
        void this.router.navigate(['/accueil']);
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 409) {
          this.registerForm.controls.email.setErrors({
            emailTaken: true
          });

          this.registerForm.controls.email.markAsTouched();
          return;
        }

        this.serverError.set("Une erreur s'est produite lors de l'inscription.");
      }
    });
  }
}