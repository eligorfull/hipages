import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {Job} from "../model/job";
import {ServerService} from "./server.service";

@Injectable({
  providedIn: 'root'
})
export class JobService {

  private jobRestUrl = `${environment.serverUrlBase}`;

  constructor(private serverService: ServerService) { }

  getAllNewJobs(jobStatus: string): Observable<Job[]> {
    return this.serverService.getRequest(`${this.jobRestUrl}/` + jobStatus, null);
  }

  changeJobStatus(id: number, newStatus: string): Observable<Job> {
    const data = {
      id,
      newStatus
    }
    return this.serverService.postRequest(`${this.jobRestUrl}/change-status`, data, null);
  }

}
