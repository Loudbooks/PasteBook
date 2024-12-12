export type Paste = {
  id: string;
  title: string;
  created: string;
  reportBook: boolean;
  wrap: boolean;
  user: {
    id: string;
  };
  expires_at: string;
};
