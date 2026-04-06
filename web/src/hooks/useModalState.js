/**
 * useModalState Custom Hook
 * 
 * PATTERN: Custom Hook (Reusable State Logic)
 * 
 * PROBLEM SOLVED:
 * - Dashboard/AdminDashboard each had 3-4 modal state setups:
 *   const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
 *   const [bookingToDelete, setBookingToDelete] = useState(null);
 *   const [isDeleting, setIsDeleting] = useState(false);
 * - Repeated in both Dashboard.jsx and AdminDashboard.jsx
 * - Difficult to coordinate multiple modals
 * 
 * BENEFITS:
 * - Eliminates repeated useState calls (3-4 per modal)
 * - Easy to add new modal states (just name them)
 * - Reusable across components
 * - Centralized modal management logic
 */
import { useState } from 'react';

export const useModalState = () => {
  const [modals, setModals] = useState({
    delete: false,
    confirm: false,
    success: false,
    edit: false
  });

  const [selectedItem, setSelectedItem] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  const openModal = (modalName, item = null) => {
    setModals(prev => ({ ...prev, [modalName]: true }));
    if (item) setSelectedItem(item);
  };

  const closeModal = (modalName) => {
    setModals(prev => ({ ...prev, [modalName]: false }));
    setSelectedItem(null);
  };

  const closeAllModals = () => {
    setModals({
      delete: false,
      confirm: false,
      success: false,
      edit: false
    });
    setSelectedItem(null);
  };

  return {
    modals,
    selectedItem,
    isLoading,
    setIsLoading,
    openModal,
    closeModal,
    closeAllModals
  };
};

export default useModalState;
