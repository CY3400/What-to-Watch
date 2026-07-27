import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';

type Variant = 'hero' | 'auth';

@Component({
  selector: 'app-public-layout',
  imports: [RouterLink],
  templateUrl: './public-layout.html',
  styleUrl: './public-layout.scss',
})
export class PublicLayout {
  @Input() variant: Variant = 'hero';
}
