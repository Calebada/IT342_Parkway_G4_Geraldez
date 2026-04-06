/**
 * useLocalStorage Custom Hook
 * 
 * PATTERN: Custom Hook + Abstraction
 * 
 * PROBLEM SOLVED:
 * - Direct localStorage.getItem/setItem calls scattered across 5+ components
 * - Magic key strings (no constants):
 *   'currentUser', 'confirmedBookings_${parkingLotId}', 'bookings'
 * - Inconsistent storage patterns
 * - No centralized validation or transformation
 * - In AuthContext, Dashboard, AdminDashboard, Profile
 * 
 * BENEFITS:
 * - Single source of truth for storage keys
 * - Type-safe storage access
 * - Automatic JSON serialization/deserialization
 * - Can easily add encryption/compression later
 * - Easy to migrate from localStorage to SessionStorage or DB
 * - Automatic cleanup on logout
 */
import { useState, useEffect } from 'react';

// Storage keys constants
export const STORAGE_KEYS = {
  CURRENT_USER: 'parkway_currentUser',
  CONFIRMED_BOOKINGS: 'parkway_confirmedBookings',
  BOOKINGS: 'parkway_bookings',
  FORM_CACHE: 'parkway_formCache'
};

/**
 * Simple storage management hook
 * @param {string} key - Storage key (use STORAGE_KEYS constants)
 * @param {*} initialValue - Default value if key doesn't exist
 */
export const useLocalStorage = (key, initialValue) => {
  const [storedValue, setStoredValue] = useState(() => {
    try {
      const item = window.localStorage.getItem(key);
      return item ? JSON.parse(item) : initialValue;
    } catch (error) {
      console.error(`Error reading from localStorage key "${key}":`, error);
      return initialValue;
    }
  });

  const setValue = (value) => {
    try {
      const valueToStore = value instanceof Function ? value(storedValue) : value;
      setStoredValue(valueToStore);
      window.localStorage.setItem(key, JSON.stringify(valueToStore));
    } catch (error) {
      console.error(`Error writing to localStorage key "${key}":`, error);
    }
  };

  const removeValue = () => {
    try {
      window.localStorage.removeItem(key);
      setStoredValue(initialValue);
    } catch (error) {
      console.error(`Error removing from localStorage key "${key}":`, error);
    }
  };

  return [storedValue, setValue, removeValue];
};

/**
 * Specialized hook for current user storage
 */
export const useCurrentUser = () => {
  const [user, setUser, clearUser] = useLocalStorage(STORAGE_KEYS.CURRENT_USER, null);

  const logout = () => {
    clearUser();
  };

  return {
    currentUser: user,
    setCurrentUser: setUser,
    logout,
    isLoggedIn: !!user
  };
};

/**
 * Specialized hook for parking lot bookings
 */
export const useBookingStorage = (parkingLotId) => {
  const key = `${STORAGE_KEYS.CONFIRMED_BOOKINGS}_${parkingLotId}`;
  const [bookings, setBookings, clearBookings] = useLocalStorage(key, []);

  const addBooking = (bookingId) => {
    setBookings(prev => {
      if (Array.isArray(prev) && !prev.includes(bookingId)) {
        return [...prev, bookingId];
      }
      return prev;
    });
  };

  const removeBooking = (bookingId) => {
    setBookings(prev =>
      Array.isArray(prev) ? prev.filter(id => id !== bookingId) : []
    );
  };

  const isBookingConfirmed = (bookingId) => {
    return Array.isArray(bookings) && bookings.includes(bookingId);
  };

  return {
    confirmedBookings: bookings,
    addBooking,
    removeBooking,
    isBookingConfirmed,
    clearBookings
  };
};

export default {
  useLocalStorage,
  useCurrentUser,
  useBookingStorage,
  STORAGE_KEYS
};
