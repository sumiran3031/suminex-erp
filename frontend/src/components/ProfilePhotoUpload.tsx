import { useState } from 'react';
import axios from 'axios';
import { uploadProfilePhoto, getFileUrl } from '../services/profileService';

export default function ProfilePhotoUpload() {
  const [photoPath, setPhotoPath] = useState<string | null>(null);
  const [file, setFile] = useState<File | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [uploading, setUploading] = useState(false);

  async function handleUpload() {
    if (!file) return;
    setError(null);
    setUploading(true);
    try {
      const result = await uploadProfilePhoto(file);
      setPhotoPath(result.profilePhotoPath);
    } catch (err) {
      if (axios.isAxiosError(err) && err.response?.data?.message) {
        setError(err.response.data.message);
      } else {
        setError('Failed to upload photo.');
      }
    } finally {
      setUploading(false);
    }
  }

  return (
    <div style={{ padding: 16, border: '1px solid #e2e8f0', borderRadius: 4, marginBottom: 16 }}>
      <h3>Profile Photo</h3>
      {photoPath && (
        <img
          src={getFileUrl(photoPath)}
          alt="Profile"
          style={{ width: 100, height: 100, objectFit: 'cover', borderRadius: '50%', marginBottom: 8, display: 'block' }}
        />
      )}
      <input type="file" accept="image/jpeg,image/png,image/webp" onChange={(e) => setFile(e.target.files?.[0] ?? null)} />
      <button onClick={handleUpload} disabled={uploading} style={{ marginLeft: 8 }}>
        {uploading ? 'Uploading...' : 'Upload'}
      </button>
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </div>
  );
}