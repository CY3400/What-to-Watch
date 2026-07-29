import { Component, OnInit, signal } from '@angular/core';
import { SeriesApi } from '../../services/series-api';
import { TvSeries } from '../../models/series.models';
import { finalize } from 'rxjs';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home implements OnInit {
  series: TvSeries[] = [];
  readonly isLoading = signal(true);
  readonly errorMessage = signal('');

  constructor(private api: SeriesApi) {}

  ngOnInit(): void {
    this.api.getPopular().pipe(finalize(() => {this.isLoading.set(false);})).subscribe({
      next: (response) => {
        this.series = response.results;
      },
      error: () => {
        this.errorMessage.set('Impossible de charger les séries.');
      }
    });
  }

  protected getPosterUrl(posterPath: string | null): string | null {
    if (!posterPath) {
      return null;
    }

    return `${environment.tmdbImageBaseUrl}${posterPath}`;
  }
}
