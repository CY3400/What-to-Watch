import { Injectable } from "@angular/core";
import { CanActivate, Router, UrlTree } from "@angular/router";
import { map, Observable } from "rxjs";
import { Auth } from "../services/auth";

@Injectable({ providedIn: 'root' })
export class GuestGuard implements CanActivate {
    constructor(private auth: Auth, private router: Router) {}

    canActivate(): Observable<boolean | UrlTree> {
        return this.auth.isAuthenticated().pipe(
            map((isAuthenticated) =>
                isAuthenticated ? this.router.createUrlTree(['/accueil']) : true
            )
        );
    }
}