/**
 * API Service Layer - Centralized API Communication
 * 
 * PATTERN: Facade + Adapter
 * 
 * PROBLEM SOLVED:
 * - 12+ hardcoded 'http://localhost:8080' URLs scattered across components
 * - No environment-based configuration
 * - Impossible to switch environments (local/dev/production)
 * - Hardcoded URLs in:
 *   - AuthContext.jsx (3 calls)
 *   - Dashboard.jsx (4 calls)
 *   - AdminDashboard.jsx (5 calls)
 *   - BookingModal.jsx (4 calls)
 *   - ParkingLocations.jsx (2 calls)
 *   - Profile.jsx (TODO: not implemented)
 * 
 * BENEFITS:
 * - Single source of truth for all API endpoints
 * - Environment-based configuration (@env.local)
 * - Easy to mock for testing
 * - Consistent error handling across app
 * - Centralized request/response transformation
 * - Easy to add authentication headers
 * - Single place to add request logging
 */

// Get API base URL from environment or default to localhost
export const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

/**
 * Parse response and handle common HTTP errors
 */
const handleResponse = async (response) => {
  if (response.status === 401) {
    // Unauthorized - clear stored user
    localStorage.removeItem('currentUser');
    window.location.href = '/login';
    throw new Error('Session expired. Please login again.');
  }
  
  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error.message || `HTTP Error: ${response.status}`);
  }
  
  return response.json();
};

/**
 * Authentication APIs
 */
export const authAPI = {
  login: async (email, password) => {
    const response = await fetch(`${API_BASE_URL}/api/users/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password }),
      credentials: 'include'
    });
    return handleResponse(response);
  },

  register: async (firstname, lastname, email, password) => {
    const response = await fetch(`${API_BASE_URL}/api/users/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ firstname, lastname, email, password }),
      credentials: 'include'
    });
    return handleResponse(response);
  },

  registerAdmin: async (businessName, email, password, capacity) => {
    const response = await fetch(`${API_BASE_URL}/api/admins/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ businessName, email, password, capacity }),
      credentials: 'include'
    });
    return handleResponse(response);
  },

  getAllAdmins: async () => {
    const response = await fetch(`${API_BASE_URL}/api/admins`, {
      credentials: 'include'
    });
    return handleResponse(response);
  }
};

/**
 * Booking APIs
 */
export const bookingAPI = {
  getUserBookings: async (userId) => {
    const response = await fetch(
      `${API_BASE_URL}/api/bookings/user/${userId}`,
      { credentials: 'include' }
    );
    return handleResponse(response);
  },

  getAdminBookings: async (adminId) => {
    const response = await fetch(
      `${API_BASE_URL}/api/bookings/admin/${adminId}`,
      { credentials: 'include' }
    );
    return handleResponse(response);
  },

  createBooking: async (bookingData) => {
    const response = await fetch(`${API_BASE_URL}/api/bookings`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(bookingData),
      credentials: 'include'
    });
    return handleResponse(response);
  },

  confirmBooking: async (bookingId) => {
    const response = await fetch(
      `${API_BASE_URL}/api/bookings/${bookingId}/confirm`,
      {
        method: 'PUT',
        credentials: 'include'
      }
    );
    return handleResponse(response);
  },

  deleteBooking: async (bookingId) => {
    const response = await fetch(
      `${API_BASE_URL}/api/bookings/${bookingId}`,
      {
        method: 'DELETE',
        credentials: 'include'
      }
    );
    return handleResponse(response);
  }
};

/**
 * Parking Lot APIs
 */
export const parkingLotAPI = {
  getAllParkingLots: async () => {
    const response = await fetch(
      `${API_BASE_URL}/api/admin/parking-lots`,
      { credentials: 'include' }
    );
    return handleResponse(response);
  },

  getParkingLotById: async (adminId) => {
    const response = await fetch(
      `${API_BASE_URL}/api/admins/${adminId}`,
      { credentials: 'include' }
    );
    return handleResponse(response);
  }
};

/**
 * Parking Slot APIs
 */
export const parkingSlotAPI = {
  initializeSlots: async () => {
    const response = await fetch(
      `${API_BASE_URL}/api/parking-slots/initialize`,
      { credentials: 'include' }
    );
    return handleResponse(response);
  },

  getSlotsByParkingLot: async (parkingLotId) => {
    const response = await fetch(
      `${API_BASE_URL}/api/parking-slots/${parkingLotId}`,
      { credentials: 'include' }
    );
    return handleResponse(response);
  },

  checkAvailability: async (parkingLotId) => {
    const response = await fetch(
      `${API_BASE_URL}/api/parking-slots/${parkingLotId}/availability`,
      { credentials: 'include' }
    );
    return handleResponse(response);
  },

  updateSlotStatus: async (slotId, status) => {
    const response = await fetch(
      `${API_BASE_URL}/api/parking-slots/${slotId}`,
      {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ status }),
        credentials: 'include'
      }
    );
    return handleResponse(response);
  }
};

/**
 * Vehicle APIs
 */
export const vehicleAPI = {
  getUserVehicles: async (userId) => {
    const response = await fetch(
      `${API_BASE_URL}/api/vehicles/user/${userId}`,
      { credentials: 'include' }
    );
    return handleResponse(response);
  },

  addVehicle: async (vehicleData) => {
    const response = await fetch(`${API_BASE_URL}/api/vehicles`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(vehicleData),
      credentials: 'include'
    });
    return handleResponse(response);
  },

  updateVehicle: async (vehicleId, vehicleData) => {
    const response = await fetch(
      `${API_BASE_URL}/api/vehicles/${vehicleId}`,
      {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(vehicleData),
        credentials: 'include'
      }
    );
    return handleResponse(response);
  },

  deleteVehicle: async (vehicleId) => {
    const response = await fetch(
      `${API_BASE_URL}/api/vehicles/${vehicleId}`,
      {
        method: 'DELETE',
        credentials: 'include'
      }
    );
    return handleResponse(response);
  }
};

const API = {
  authAPI,
  bookingAPI,
  parkingLotAPI,
  parkingSlotAPI,
  vehicleAPI
};

export default API;
