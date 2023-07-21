import React, { ChangeEvent, FC, useState } from 'react';
import './App.css';
import { Membership } from './Type';
import { fetchMembers } from './Api';
import { Button, Input, Modal, ModalBody, ModalHeader } from 'reactstrap';
import turnitinLogo from './turnitin-logo.png';

const App: FC<any> = () => {
  //State for storing memberships, search input, and the active membership for modal display
  const [memberships, setMemberships] = useState<Array<Membership>>([]);
  const [search, setSearch] = useState<string>();
  const [activeMembership, setActiveMembership] = useState<Membership | undefined>();

  //Function to fetch memberships from the backend and update the state
  const loadMemberships = () => {
    return fetchMembers()
      .then(membershipList => setMemberships(membershipList.memberships));
  };

  //Function to update the search input when the user types in the search box
  const updateSearch = (event: ChangeEvent<HTMLInputElement>) => {
    setSearch(event.target.value.toLowerCase()); // Convert search input to lowercase for case-insensitive search
  };

  //Function to open the modal and display the details of a membership
  const loadDetailsModal = (membership: Membership) => {
    setActiveMembership(membership);
  };

  //Function to close the modal
  const closeDetailsModal = () => {
    setActiveMembership(undefined);
  };

  return (
    <div className="App">
      <header className="App-header">
        <img src={turnitinLogo} alt='logo' />
        <div className='user-inputs'>
          {/* Button to fetch memberships */}
          <Button color='primary' className='fetch-btn' onClick={loadMemberships}>
            Fetch Memberships
          </Button>
          {/* Search input box */}
          <Input type='text' placeholder='Search' onChange={updateSearch} />
        </div>
        {/* Render table if there are memberships and at least one matches the search */}
        {memberships.length > 0 && memberships.some(membership => {
          const { user } = membership;
          return (
            !search ||
            user?.name.toLowerCase().includes(search) ||
            user?.email.toLowerCase().includes(search)
          );
        }) && (
          <table>
            <thead>
              <tr>
                <th>Name</th>
                <th>Email</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {/* Map through memberships and display rows for matching memberships */}
              {memberships.map(membership => {
                const { user } = membership;
                if (
                  !search ||
                  user?.name.toLowerCase().includes(search) ||
                  user?.email.toLowerCase().includes(search)
                ) {
                  return (
                    <tr key={membership.id}>
                      <td>{user?.name}</td>
                      <td>{user?.email}</td>
                      <td>
                        <Button color='primary' outline onClick={() => loadDetailsModal(membership)}>
                          Details
                        </Button>
                      </td>
                    </tr>
                  );
                }
                return null;
              })}
            </tbody>
          </table>
        )}
        {/* Modal to display membership details */}
        {activeMembership && (
          <Modal isOpen={!!activeMembership}>
            <ModalHeader toggle={closeDetailsModal}>User Details</ModalHeader>
            <ModalBody>
              <div>
                <p>Name: {activeMembership.user?.name}</p>
                <p>Email: {activeMembership.user?.email}</p>
                <p>Membership ID: {activeMembership.id}</p>
                <p>User ID: {activeMembership.user?.id}</p>
                <p>Role: {activeMembership.role}</p>
              </div>
            </ModalBody>
          </Modal>
        )}
      </header>
    </div>
  );
};

export default App;
