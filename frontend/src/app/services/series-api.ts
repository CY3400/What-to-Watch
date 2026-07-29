import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { TvSeriesResponse } from "../models/series.models";
import { Observable } from "rxjs";

@Injectable({ providedIn: 'root' })
export class SeriesApi {
    private readonly baseUrl = environment.apiBaseUrl;

    constructor(private http: HttpClient) {}

    getPopular(): Observable<TvSeriesResponse> {
        return this.http.get<TvSeriesResponse>(`${this.baseUrl}/series/popular`);
    }
}