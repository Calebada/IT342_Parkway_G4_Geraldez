/**
 * useFormHandler Custom Hook
 * 
 * PATTERN: Custom Hook (Reusable State Logic)
 * 
 * PROBLEM SOLVED:
 * - Form handling code repeated in 5+ components:
 *   - Login.jsx
 *   - Register.jsx
 *   - Profile.jsx
 *   - BookingModal.jsx
 *   - AdminDashboard.jsx (profile section)
 * - Each had duplicate:
 *   const [formData, setFormData] = useState({...});
 *   const handleChange = (e) => { ... }
 *   const handleSubmit = (e) => { ... }
 *   const [error, setError] = useState('');
 * 
 * BENEFITS:
 * - DRY (Don't Repeat Yourself)
 * - Consistent form validation approach
 * - Reusable error handling
 * - Easy to add form features (show/hide password, validation, etc.)
 * - Type-safe if converted to TypeScript
 */
import { useState } from 'react';

export const useFormHandler = (initialFormData, onSubmit) => {
  const [formData, setFormData] = useState(initialFormData);
  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    // Clear field error on change
    if (errors[name]) {
      setErrors(prev => {
        const newErrors = { ...prev };
        delete newErrors[name];
        return newErrors;
      });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors({});
    setSuccessMessage('');
    setIsSubmitting(true);

    try {
      await onSubmit(formData);
      setSuccessMessage('Success!');
      setFormData(initialFormData);
    } catch (error) {
      setErrors({ submit: error.message });
    } finally {
      setIsSubmitting(false);
    }
  };

  const resetForm = () => {
    setFormData(initialFormData);
    setErrors({});
    setSuccessMessage('');
  };

  const setFieldError = (fieldName, errorMessage) => {
    setErrors(prev => ({
      ...prev,
      [fieldName]: errorMessage
    }));
  };

  return {
    formData,
    setFormData,
    errors,
    setFieldError,
    isSubmitting,
    successMessage,
    handleChange,
    handleSubmit,
    resetForm
  };
};

export default useFormHandler;
