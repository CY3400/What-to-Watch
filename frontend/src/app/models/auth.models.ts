export type LoginRequest = {
  email: string,
  password: string
};

export type RegisterRequest = {
  email: string,
  password: string
};

export type UserRole = 'USER' | 'ADMIN';

export type UserSessionResponse = {
  userId: number,
  email: string,
  role: UserRole
};