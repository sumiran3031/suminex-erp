import { createContext, useContext, useState, useEffect, type ReactNode } from 'react';
import type { AuthUser, LoginRequest } from '../types/auth';
import { login as loginApi } from '../services/authService';

interface AuthContextValue {
  user: AuthUser | null;
  isAuthenticated: boolean;
  login: (credentials: LoginRequest) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(null);

  // On app load, restore the session from localStorage if a token exists —
  // this is what keeps a user logged in across page refreshes.
  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    const storedToken = localStorage.getItem('token');
    if (storedUser && storedToken) {
      setUser(JSON.parse(storedUser));
    }
  }, []);

  async function login(credentials: LoginRequest) {
    const response = await loginApi(credentials);
    const authUser: AuthUser = { email: response.email, role: response.role };

    localStorage.setItem('token', response.token);
    localStorage.setItem('user', JSON.stringify(authUser));
    setUser(authUser);
  }

  function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setUser(null);
  }

  return (
    <AuthContext.Provider value={{ user, isAuthenticated: !!user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth(): AuthContextValue {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}