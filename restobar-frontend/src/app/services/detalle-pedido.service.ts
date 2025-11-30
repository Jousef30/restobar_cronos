import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DetallePedidoService {

  private apiUrl = 'http://localhost:8080/api/detallepedido';

  constructor(private http: HttpClient) {}

  private getToken() {
    return localStorage.getItem('token');
  }

  private getHeaders() {
    return {
      headers: new HttpHeaders({
        'Authorization': 'Bearer ' + this.getToken(),
        'Content-Type': 'application/json'
      })
    };
  }

  listar(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl, this.getHeaders());
  }

  crear(data: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, data, this.getHeaders());
  }

  actualizar(id: number, data: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, data, this.getHeaders());
  }

  eliminar(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      headers: this.getHeaders().headers,
      responseType: 'text'
    });
  }
}
