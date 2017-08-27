import cv2
import os
import numpy as np


def get_filepaths(directory):
    """
    This function will generate the file names in a directory
    tree by walking the tree either top-down or bottom-up. For each
    directory in the tree rooted at directory top (including top itself),
    it yields a 3-tuple (dirpath, dirnames, filenames).
    """
    file_paths = []  # List which will store all of the full filepaths.

    # Walk the tree.
    for root, directories, files in os.walk(directory):
        for filename in files:
            # Join the two strings in order to form the full filepath.
            filepath = os.path.join(root, filename)
            file_paths.append(filepath)  # Add it to the list.

    return file_paths  # Self-explanatory.


if __name__ == "__main__":
    list_images = []
    img_directory = "input"
    file_paths = get_filepaths(img_directory)
    orig = cv2.imread(file_paths[1])
    orig = cv2.resize(orig, None, fx=0.14, fy=0.14)
    cv2.imwrite("output/output_orig.jpg", orig)
    orig_gray = cv2.cvtColor(orig, cv2.COLOR_BGR2GRAY)
    sz = orig.shape
    list_images.append(orig)
    for i, f in enumerate(file_paths[2:]):
        # Read image and convert to grayscale
        img = cv2.imread(f)
        img = cv2.resize(img, None, fx=0.14, fy=0.14)
        img_gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        warp_mode = cv2.MOTION_EUCLIDEAN
        # Define 2x3 or 3x3 matrices and initialize the matrix to identity
        if warp_mode == cv2.MOTION_HOMOGRAPHY :
            warp_matrix = np.eye(3, 3, dtype=np.float32)
        else:
            warp_matrix = np.eye(2, 3, dtype=np.float32)
        # Specify the number of iterations.
        number_of_iterations = 200

        # Specify the threshold of the increment
        # in the correlation coefficient between two iterations
        termination_eps = 1e-10

        # Define termination criteria
        criteria = (cv2.TERM_CRITERIA_EPS | cv2.TERM_CRITERIA_COUNT, number_of_iterations,  termination_eps)

        # Run the ECC algorithm. The results are stored in warp_matrix.
        (cc, warp_matrix) = cv2.findTransformECC(orig_gray, img_gray, warp_matrix, warp_mode, criteria)

        if warp_mode == cv2.MOTION_HOMOGRAPHY:
            # Use warpPerspective for Homography 
            img_aligned = cv2.warpPerspective(img, warp_matrix, (sz[1],sz[0]), flags=cv2.INTER_LINEAR + cv2.WARP_INVERSE_MAP)
        else:
            # Use warpAffine for Translation, Euclidean and Affine
            img_aligned = cv2.warpAffine(img, warp_matrix, (sz[1], sz[0]), flags=cv2.INTER_LINEAR + cv2.WARP_INVERSE_MAP)
        cv2.imwrite("output/output" + str(i) + ".jpg", img_aligned)
        list_images.append(img_aligned)

    median = np.median(list_images, axis=0)
    cv2.imwrite("output/output_median.jpg", median)
        