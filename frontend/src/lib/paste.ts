export type Paste = {
  id: string;
  title: string;
  created: string;
  wrap: boolean;
  user: {
    id: string;
  };
  expires_at: string;
};
