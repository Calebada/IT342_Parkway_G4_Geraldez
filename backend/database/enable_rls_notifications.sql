-- Enable Row Level Security (RLS) on notifications table
-- This prevents users from accessing notifications not meant for them

-- 1. Enable RLS on notifications table
ALTER TABLE notifications ENABLE ROW LEVEL SECURITY;

-- 2. Create Policy 1: Users can only SELECT their own notifications
CREATE POLICY "Users can view their own notifications"
  ON notifications FOR SELECT
  USING (auth.uid()::bigint = recipient_user_id);

-- 3. Create Policy 2: Users can UPDATE (mark as read) only their own notifications
CREATE POLICY "Users can update their own notifications"
  ON notifications FOR UPDATE
  USING (auth.uid()::bigint = recipient_user_id)
  WITH CHECK (auth.uid()::bigint = recipient_user_id);

-- 4. Create Policy 3: Allow backend service to INSERT notifications
-- (Backend uses service_role key which bypasses RLS for INSERT operations)
CREATE POLICY "Backend can insert notifications"
  ON notifications FOR INSERT
  WITH CHECK (true);

-- 5. Create Policy 4: Admin notifications - admins can view their own
CREATE POLICY "Admins can view their notifications"
  ON notifications FOR SELECT
  USING (
    recipient_role = 'admin' 
    AND auth.uid()::bigint = recipient_user_id
  );

-- 6. Grant permissions to authenticated users for client-side operations
GRANT SELECT, UPDATE ON public.notifications TO authenticated;

-- 7. Grant INSERT permission to service_role (backend only)
GRANT INSERT ON public.notifications TO service_role;
