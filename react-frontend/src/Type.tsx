export type User = {
    id: String;
    name: String;
    email: String;
}

export type Membership = {
    id: string;
    user_id: string;
    role: string;
    user: User;
}
export type MembershipList = {
    memberships: Membership[];
}
