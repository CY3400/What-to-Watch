import { Routes } from '@angular/router';
import { Welcome } from './pages/welcome/welcome';
import { Register } from './pages/register/register';
import { Login } from './pages/login/login';
import { GuestGuard } from './guards/guest-guard';
import { Home } from './pages/home/home';
import { AuthGuard } from './guards/auth-guard';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'bienvenue'
    },
    {
        path: 'bienvenue',
        component: Welcome,
        title: 'What to Watch - Bienvenue',
        canActivate: [GuestGuard]
    },
    {
        path:'s-enregistrer',
        component: Register,
        title: 'What to Watch - Inscription',
        canActivate: [GuestGuard]
    },
    {
        path:'se-connecter',
        component: Login,
        title: 'What to Watch - Connexion',
        canActivate: [GuestGuard]
    },
    {
        path:'accueil',
        component: Home,
        title: 'What to Watch - Accueil',
        canActivate: [AuthGuard]
    },
    {
        path: '**',
        redirectTo: 'bienvenue'
    }
];
