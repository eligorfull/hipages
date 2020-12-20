import {Suburb} from "./suburb";
import {Category} from "./category";

export class Job {
  id: number;
  status: string;
  contactName: string;
  contactPhone: string;
  contactEmail: string;
  price: string;
  description: string;
  createdAt: Date;
  updatedAt: Date;
  suburb: Suburb;
  category: Category;

  constructor() {}
}
