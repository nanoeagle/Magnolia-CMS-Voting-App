function enableDisableTextField(otherRadioBtnId, voterOwnAnswerId) {
  var otherRadioBtn = document.getElementById(otherRadioBtnId);
  var answerTextField = document.getElementById(voterOwnAnswerId);
  
  // The answerTextField is enabled only if the otherRadioBtn is checked.
  answerTextField.disabled = otherRadioBtn.checked ? false : true;
  
  // If the answerTextField is enabled.
  if (!answerTextField.disabled) {
    answerTextField.focus();
    answerTextField.required = true;
  } else {
    answerTextField.required = false;
  }
}