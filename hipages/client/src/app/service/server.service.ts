import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from '@angular/common/http';
import {catchError, map} from 'rxjs/operators';
import {BehaviorSubject, Observable, throwError} from 'rxjs';

/*
 * Class used to wrap get requests for calls to endpoints that are commonly used (i.e. called in multiple components)
 *  or should be allowed to be repeated. Primary use is to prevent repeatedly calling the API when a service is used
 *  in multiple places.
 */
export class BufferedGetRequest<T> {
  private lastCallTime: number;
  private subject: BehaviorSubject<T>;

  constructor(private callback: () => Observable<T>, private bufferTime: number = 1000, initialValue: T = null) {
    this.subject = new BehaviorSubject<T>(initialValue);
  }

  public getValue(): Observable<T> {
    if (!this.lastCallTime || this.lastCallTime < Date.now() - this.bufferTime) {
      this.lastCallTime = Date.now();
      this.callback().subscribe(value => {
        this.subject.next(value);
      });
    }
    return this.subject.asObservable();
  }
}

@Injectable({
  providedIn: 'root'
})
export class ServerService {

  constructor(
    private httpClient: HttpClient
  ) {}

  /*
   * The get observable object used in get
   */
  public getObservable(url: string, requestOptions): Observable<any> {
    return this.httpClient.get(url, requestOptions);
  }

  /*
   * Making get request
   */
  public getRequest(url: string, urlSearchParams: HttpParams): Observable<any> {

    const httpHeaders = new HttpHeaders()
      .set('Cache-Control', 'no-cache')
      .set('Pragma', 'no-cache')
      .set('Expires', 'Sat, 01 Jan 2000 00:00:00 GMT');

    const requestOptions = {
      params: urlSearchParams,
      headers: httpHeaders
    };

    return this.getObservable(url, requestOptions)
      .pipe(
        catchError((error: HttpErrorResponse) =>
          this.handleError(error)
        )
      );
  }

  /*
   * Returns an observable for a request returning a single value with the response unboxed to a given type (via the
   *  responseMapper closure).
   */
  public getValue<T>(url: string, urlSearchParams: HttpParams, responseMapper: (response: any, index?: number) => T): Observable<T> {
    return this.getRequest(url, urlSearchParams).pipe(
      map(responseMapper)
    );
  }

  /*
   * Returns a repeatable, shared object that will allow multiple subscribers to an observer returning a single value.
   * Each call to getValue() will check if the time elapsed since the last call is > the given buffer time and if so
   *  will trigger a new get request, returning an observable that will yield the last value and will continue to be
   *  updated.
   */
  public bufferedGetValue<T>(
    url: string,
    urlSearchParams: HttpParams,
    responseMapper: (response: any, index?: number) => T,
    bufferTime: number = 1000
  ) {
    return new BufferedGetRequest<T>(this.getValue.bind(this, url, urlSearchParams, responseMapper), bufferTime);
  }

  /*
   * Returns an observable for a request returning a collection of values with the response unboxed to a given type
   *  (where the responseMapper closure is called on each value in the collection).
   */
  public getCollection<T>(url: string, urlSearchParams: HttpParams, responseMapper: (response: any, index?: number) => T): Observable<T[]> {
    return this.getRequest(url, urlSearchParams).pipe(
      map(response => response.map(responseMapper))
    );
  }

  /*
   * Returns a repeatable, shared object that will allow multiple subscribers to an observer returning a collection of
   *  values.
   * Each call to getValue() will check if the time elapsed since the last call is > the given buffer time and if so
   *  will trigger a new get request, returning an observable that will yield the last value and will continue to be
   *  updated.
   */
  public bufferedGetCollection<T>(
    url: string,
    urlSearchParams: HttpParams,
    responseMapper: (response: any, index?: number) => T,
    bufferTime: number = 1000
  ) {
    return new BufferedGetRequest<T[]>(this.getCollection.bind(this, url, urlSearchParams, responseMapper), bufferTime, []);
  }

  /*
   * The post observable object used in get
   */
  public postObservable(url: string, data: any, requestOptions): Observable<any> {
    return this.httpClient.post(url, data, requestOptions);
  }

  public postRequest(url: string, data: any, httpParams: HttpParams): Observable<any> {
    const httpHeaders = new HttpHeaders()
      .set('Content-type', 'application/json');

    const requestOptions = {
      params: httpParams,
      headers: httpHeaders
    };

    return this.postObservable(url, data, requestOptions)
      .pipe(
        catchError((error: HttpErrorResponse) =>
          this.handleError(error)
        )
      );
  }

  /*
   * The post observable object used in get
   */
  public putObservable(url: string, data: any, requestOptions): Observable<any> {
    return this.httpClient.put(url, data, requestOptions);
  }

  public putRequest(url: string, data: any, httpParams: HttpParams): any {
    const httpHeaders = new HttpHeaders()
      .set('Content-type', 'application/json');

    const requestOptions = {
      params: httpParams,
      headers: httpHeaders
    };

    return this.httpClient.put<any>(url, data, requestOptions)
      .pipe(
        catchError((error: HttpErrorResponse) =>
          this.handleError(error)
        )
      );
  }

  public deleteRequest(url: string, data: any, httpParams: HttpParams): any {
    const httpHeaders = new HttpHeaders()
      .set('Content-type', 'application/json');

    const requestOptions = {
      params: httpParams,
      headers: httpHeaders,
      body: JSON.stringify(data)
    };

    return this.httpClient.delete(url, requestOptions)
      .pipe(
        catchError((error: HttpErrorResponse) =>
          this.handleError(error)
        )
    );
  }

  private handleError(error: HttpErrorResponse | any) {
    let errorMessage: string;
    if (error instanceof HttpErrorResponse) {
      errorMessage = error.status + ' - ' + (error.statusText || '');
    } else {
      errorMessage = error.message ? error.message : error.toString();
    }

    console.error(error);
    return throwError('Error: ' + errorMessage);
  }

  logout() {
    window.location.href = '/tc7/Shibboleth.sso/Logout';
  }

  public getFile(url: string): Observable<Blob> {
    return this.httpClient.get(url, { responseType: 'blob' });
  }

}
