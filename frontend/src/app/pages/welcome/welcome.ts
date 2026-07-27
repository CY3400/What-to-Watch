import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PublicLayout } from '../../shared/components/public-layout/public-layout';

@Component({
  selector: 'app-welcome',
  imports: [RouterLink, PublicLayout],
  templateUrl: './welcome.html',
  styleUrl: './welcome.scss',
})
export class Welcome {}
