export interface TvSeries {
    id: number;
    name: string;
    overview: string;
    firstAirDate: string;
    posterPath: string | null;
    voteAverage: number;
}

export interface TvSeriesResponse {
    page: number;
    results: TvSeries[];
}