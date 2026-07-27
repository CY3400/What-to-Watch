import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

export function passwordStrengthValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        const password = String(control.value ?? '');

        if (!password) {
            return null;
        }

        const hasUpperCase = /[A-Z]/.test(password);
        const hasLowerCase = /[a-z]/.test(password);
        const hasDigit = /[0-9]/.test(password);
        const hasSpecialCharacter = /[^A-Za-z0-9\s]/.test(password);
        const hasOnlyPrintableAscii = /^[\x20-\x7E]+$/.test(password);

        if (!hasOnlyPrintableAscii) {
            return { passwordCharacters: true };
        }

        const isValid = hasUpperCase && hasLowerCase && hasDigit && hasSpecialCharacter;

        return isValid ? null : { passwordStrength: true };
    }
}

export function passwordMatchValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        const password = control.get('password')?.value;
        const confirmPassword = control.get('confirmPassword')?.value;

        if (!password || !confirmPassword) {
            return null;
        }
        
        return password === confirmPassword ? null : {passwordMismatch: true};
    }
}