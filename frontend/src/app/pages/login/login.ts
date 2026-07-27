import { Component, signal } from '@angular/core';
import { PublicLayout } from '../../shared/components/public-layout/public-layout';
import { Router, RouterLink } from '@angular/router';
import { Icon } from '../../shared/components/icon/icon';
import { FormControl , FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Auth } from '../../services/auth';
import { LoginRequest } from '../../models/auth.models';
import { finalize } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-login',
  imports: [RouterLink, PublicLayout, Icon, ReactiveFormsModule],
  templateUrl: './login.html'
})
export class Login {
  showPassword: boolean = false;
  readonly isSubmitting = signal(false);
  readonly serverError = signal('');

  loginForm = new FormGroup(
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
          Validators.maxLength(64)
        ]
      })
    }
  );

  constructor(private auth: Auth, private router: Router) {}

  protected togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  private validateSubmit(): boolean {
    this.loginForm.markAllAsTouched();
    return this.loginForm.valid;
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

    const formValue = this.loginForm.getRawValue();
    const user: LoginRequest = {
      email: formValue.email.trim(),
      password: formValue.password
    };

    this.auth.login(user).pipe(finalize(() => {this.isSubmitting.set(false);})).subscribe({
      next: () => {
        void this.router.navigate(['/accueil']);
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 401) {
          this.serverError.set('Adresse e-mail ou mot de passe incorrect.');
          return;
        }

        this.serverError.set("Une erreur s'est produite lors de la connexion.");
      }
    });
  }
}
