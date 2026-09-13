import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MarketData } from '../model/market-data';

@Injectable({
  providedIn: 'root'
})
export class MarketDataService {

  private readonly apiUrl = 'http://localhost:8080/market-data';

  constructor(private http: HttpClient) {
  }

  getById(id: number) {
    return this.http.get<MarketData>(`${this.apiUrl}/${id}`);
  }

  insert(marketDataList: MarketData[]) {
    return this.http.post<void>(this.apiUrl, marketDataList);
  }

  update(id: number, marketData: MarketData) {
    return this.http.put<MarketData>(
      `${this.apiUrl}/${id}`,
      marketData
    );
  }

  delete(id: number) {
    return this.http.delete<void>(
      `${this.apiUrl}/${id}`
    );
  }

  uploadFile(file: File) {
    const formData = new FormData();

    formData.append('fichier', file);

    return this.http.post<void>(
      `${this.apiUrl}/files`,
      formData
    );
  }
}
